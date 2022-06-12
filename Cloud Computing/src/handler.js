const mongoose = require("mongoose");
const Item = require("./model/Item");
const Transaction = require("./model/Transaction");
const User = require("./model/User");
const Xendit = require("xendit-node");
const path = require("path");
const serviceKey = path.join("key/key.json");
const { Storage } = require("@google-cloud/storage");

// ** CLOUD STORAGE ** \\
const storage = new Storage({
   projectId: "excellent-ship-351309",
   keyFilename: serviceKey,
});
const bucket = storage.bucket("djoalan-item-image-bucket"); // Get this from Google Cloud -> Storage

// ** XENDIT ** \\
const x = new Xendit({
   secretKey: "xnd_development_paOBsN1tWc428C0QwKBsrOw7i2lWdmzlsRNjVohUy7z5GCpym8u8Cclqh31JYKn",
});

// Setup connection with mongoose
mongoose
   .connect("mongodb://34.124.224.131:27017/djoalan?directConnection=true")
   .then(() => {
      console.log("database connected");
   })
   .catch((err) => console.log(err));

// ==== USER HANDLERS ==== \\
// Get all users
const getAllUser = async (req, h) => {
   const offset = Number(req.query.offset) || 0;
   const user = await req.mongo.db.collection("users").find({}).sort({ metacritic: -1 }).skip(offset).limit(20).toArray();

   // count number of records in database
   const status = await req.mongo.db.collection("users").count();
   console.log(status);

   // check if collection is populated
   if (status > 0) {
      const response = h.response({
         error: false,
         message: "user retrieved",
         data: [user],
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "no user retrieved",
   });
   response.code(404);
   return response;
};

// Add a new user to the collection
const signUp = async (req, h) => {
   const payload = req.payload;

   const checkEmail = await req.mongo.db.collection("users").findOne({ email: req.payload.email });

   if (!checkEmail) {
      await req.mongo.db.collection("users").insertOne(payload);
      const response = h.response({
         error: false,
         message: "user added to database",
         data: {
            payload,
         },
      });
      response.code(201);
      return response;
   }

   const response = h.response({
      error: true,
      message: "email already exist",
   });
   response.code(404);
   return response;
};

// Check user by name and password
const signIn = async (req, h) => {
   const email = req.payload.email;
   const password = req.payload.password;

   const checkEmail = await req.mongo.db.collection("users").findOne({ email });

   if (checkEmail && checkEmail.password === password) {
      console.log("user exist");

      req.cookieAuth.set({ email: req.payload.email, password: req.payload.password });

      const response = h.response({
         error: false,
         message: "user retrieved",
         data: {
            checkEmail,
         },
      });
      response.code(200);
      return response;
   } else {
      console.log("user does not exit");

      const response = h.response({
         error: true,
         message: "user doesnt exist",
      });
      response.code(500);
      return response;
   }
};

// Get user by their id params
const getUserById = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;
   const user = await req.mongo.db.collection("users").findOne({ _id: new ObjectID(id) });

   if (user) {
      const response = h.response({
         error: false,
         message: "user retrieved",
         data: {
            user,
         },
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// Update the details of a user
const updateUser = async (req, h) => {
   const payload = req.payload;

   // check payload if field exists inside collection
   const checkPayload = await User.findOne({ _id: req.params.id });

   if (checkPayload !== undefined) {
      console.log("field exist");

      checkPayload.isBusinessAcc = true;
      checkPayload.storeName = payload.storeName;
      checkPayload.company = payload.company;
      checkPayload.storeLocation.lat = payload.storeLocation.lat;
      checkPayload.storeLocation.lon = payload.storeLocation.lon;
      await checkPayload.save();

      if (checkPayload) {
         const response = h.response({
            error: false,
            message: "user updated",
            data: {
               checkPayload,
            },
         });
         response.code(201);
         return response;
      }
   } else {
      console.log("field does not exist");

      const response = h.response({
         error: true,
         message: "field does not exist",
      });
      response.code(500);
      return response;
   }

   const response = h.response({
      error: true,
      message: "someting went wrong",
   });
   response.code(500);
   return response;
};

// Delete user based on mongodb objectId
const deleteUser = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;
   const checkId = await req.mongo.db.collection("users").findOne({ _id: new ObjectID(id) });

   if (checkId) {
      await req.mongo.db.collection("users").deleteOne({ _id: ObjectID(id) });

      const response = h.response({
         error: false,
         message: "user has been deleted",
         data: {
            checkId,
         },
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "objectId does not exist",
   });
   response.code(500);
   return response;
};

// Destroy session cookie user logout
const logout = async (req, h) => {
   req.cookieAuth.clear();
   const response = h.response({
      error: false,
      message: "user has logged out",
   });
   response.code(201);
   return response;
};

// ==== ITEM HANDLERS ====
// Get list of item from stock
const getItemStock = async (req, h) => {
   let email = req.auth.credentials.email;
   const companyId = await User.findOne({ email });
   let foundProduct = await Item.find({ companyId });

   if (foundProduct !== undefined) {
      const response = h.response({
         error: false,
         message: "item retrieved",
         data: foundProduct,
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "item does not exist",
   });
   response.code(500);
   return response;
};

// Add an item to the collection
const addItem = async (req, h) => {
   const payload = req.payload;
   const email = req.auth.credentials.email;

   let getBusinessId = await User.findOne({ email });

   console.log(getBusinessId.isBusinessAcc);

   if (getBusinessId.isBusinessAcc === true) {
      if (payload !== undefined) {
         console.log("File found, trying to upload...");
         const blob = bucket.file(payload.imageUrl.hapi.filename);
         const blobStream = blob.createWriteStream();
         blobStream.on("finish", () => {
            console.log("Success");
         });
         blobStream.end(payload.imageUrl._data);

         const newItem = new Item({
            productId: payload.productId,
            name: payload.name,
            brand: payload.brand,
            expiredDate: payload.expiredDate,
            price: payload.price,
            category: payload.category,
            quantity: payload.quantity,
            imageUrl: `https://storage.googleapis.com/djoalan-item-image-bucket/${payload.imageUrl.hapi.filename}`,
            companyId: getBusinessId._id,
         });
         console.log(newItem);
         await newItem.save();

         const response = h.response({
            error: false,
            message: "item added to collection",
            newItem,
         });
         response.code(201);
         return response;
      }

      const response = h.response({
         error: true,
         message: "something went wrong",
      });
      response.code(500);
      return response;
   }

   const response = h.response({
      error: true,
      message: "BussinessAcc is false",
   });
   response.code(500);
   return response;
};

// Add items to user cart
const addToCart = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;

   // get all business itens
   const getItems = await Item.where({ companyId: ObjectID(id), productId: req.payload.productId });

   let tmp;
   getItems.forEach((element) => {
      tmp = element.productId;
   });

   // check if product id eists
   if (tmp === req.payload.productId) {
      console.log("item exists");
      const response = h.response({
         error: false,
         message: "items exist",
         getItems,
      });
      response.code(201);
      return response;
   }

   const response = h.response({
      error: true,
      message: "item does not exist",
   });
   response.code(500);
   return response;
};

// Update items from collection
const updateItem = async (req, h) => {
   const payload = req.payload;

   let checkItemId = await Item.findOne({ _id: req.params.id });

   if (checkItemId !== undefined) {
      console.log("field exist");

      if (req.payload.imageUrl) {
         console.log("File found, trying to upload...");
         const blob = bucket.file(payload.imageUrl.hapi.filename);
         const blobStream = blob.createWriteStream();
         blobStream.on("finish", () => {
            console.log("Success");
         });
         blobStream.end(payload.imageUrl._data);

         checkItemId.productId = payload.productId;
         checkItemId.name = payload.name;
         checkItemId.brand = payload.brand;
         checkItemId.expiredDate = payload.expiredDate;
         checkItemId.price = payload.price;
         checkItemId.category = payload.category;
         checkItemId.quantity = payload.quantity;
         checkItemId.imageUrl = `https://storage.googleapis.com/djoalan-item-image-bucket/${payload.imageUrl.hapi.filename}`;
         await checkItemId.save();

         const response = h.response({
            error: false,
            message: "items updated",
            data: {
               checkItemId,
            },
         });
         response.code(200);
         return response;
      }
   } else {
      const response = h.response({
         error: true,
         message: "field does not exist",
      });
      response.code(500);
      return response;
   }

   const response = h.response({
      error: true,
      message: "someting went wrong",
   });
   response.code(500);
   return response;
};

// Delete item based in objectId mongodb
const deleteItem = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;

   const checkId = await req.mongo.db.collection("items").findOne({ _id: new ObjectID(id) });

   if (checkId) {
      await req.mongo.db.collection("items").deleteOne({ _id: ObjectID(id) });

      const response = h.response({
         error: false,
         message: "item has been deleted",
         data: {
            checkId,
         },
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "objectId does not exist",
   });
   response.code(500);
   return response;
};

// Get best price
const bestPrice = async (req, h) => {
   const productId = "8991002105652"; // kopi kapal api
   // const productId_2 = "8993496103015";   // minyak sania
   // const productId_3 = "8999999015725";   // rinso molto
   // const productId_4 = "8851111401802";   //pempers

   // Get json array for getItem and parse it
   const getItem = await Item.find()
      .where({ productId: productId })
      .populate({ path: "companyId", populate: { path: "_id" } });

   console.log(getItem);
   let test = JSON.stringify(getItem);
   test = JSON.parse(test);

   for (let i = 0; i < test.length; i++) {
      delete test[i]["productId"];
      delete test[i]["brand"];
      delete test[i]["expiredDate"];
      delete test[i]["category"];
      delete test[i]["quantity"];
      delete test[i]["__v"];
      delete test[i]["companyId"]["storeLocation"];
      delete test[i]["companyId"]["nama"];
      delete test[i]["companyId"]["email"];
      delete test[i]["companyId"]["password"];
      delete test[i]["companyId"]["isBusinessAcc"];
      delete test[i]["companyId"]["storeName"];
   }
   const prices = test.sort((a, b) => a.price - b.price);

   if (getItem !== undefined) {
      const response = h.response({
         error: false,
         message: "get best price",
         sortedPrice: prices,
      });
      response.code(201);
      return response;
   }

   const response = h.response({
      error: true,
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// ==== TRANSACTION HANDLERS ====
// Get all Transactions
const getTransaction = async (req, h) => {
   const userTransaction = await Transaction.find({ userId: req.params.id }).populate("listOfItems");
   console.log(userTransaction);

   if (userTransaction) {
      const response = h.response({
         error: false,
         message: "user transaction retrieved",
         data: {
            userTransaction,
         },
      });
      response.code(201);
      return response;
   }

   const response = h.response({
      error: true,
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// Add transaction
const addTransaction = async (req, h) => {
   const { productId } = req.payload;
   const email = req.auth.credentials.email;

   let getUserId = await User.findOne({ email });

   let foundItem = await Item.find({ productId });

   let total = 0;
   foundItem.forEach((element) => {
      total += element.price;
   });

   if (foundItem) {
      const newTransaction = new Transaction({
         listOfItems: foundItem,
         userId: getUserId._id,
         total: total,
      });
      console.log(newTransaction);
      await newTransaction.save();

      const response = h.response({
         error: false,
         message: "transaction data added",
         data: {
            newTransaction,
         },
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      error: true,
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// Request payment from xendit
const requestPayment = async (req, h) => {
   const email = req.auth.credentials.email;
   let getReferencenId = await User.findOne({ email });
   console.log(getReferencenId._id);

   const { EWallet } = x;
   const ewalletSpecificOptions = {};
   const ew = new EWallet(ewalletSpecificOptions);

   if (email !== undefined) {
      try {
         const resp = await ew.createEWalletCharge({
            referenceID: getReferencenId._id,
            currency: "IDR",
            amount: Number(req.payload.amount),
            checkoutMethod: "ONE_TIME_PAYMENT",
            channelCode: req.payload.channelCode,
            channelProperties: {
               successRedirectURL: "https://dashboard.xendit.co/register/1",
            },
            metadata: {
               branch_code: "tree_branch",
            },
         });
         console.log(resp);
         await req.mongo.db.collection("requestPayment").insertOne(resp);

         const response = h.response({
            error: false,
            message: "get user payment",
            resp,
         });
         response.code(200);
         return response;
      } catch (error) {
         console.log(error);
      }
   }

   const response = h.response({
      error: true,
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// Check status payment request
const checkPayment = async (req, h) => {
   const { EWallet } = x;
   const ewalletSpecificOptions = {};
   const ew = new EWallet(ewalletSpecificOptions);

   const chargeID = req.params.id;
   console.log(chargeID);

   const resp = await ew.getEWalletChargeStatus({
      chargeID: chargeID,
   });
   console.log(resp);

   const response = h.response({
      error: false,
      message: "check user payment",
      resp,
   });
   response.code(201);
   return response;
};

module.exports = {
   getAllUser,
   signUp,
   signIn,
   getUserById,
   updateUser,
   deleteUser,
   logout,
   getItemStock,
   addItem,
   addToCart,
   updateItem,
   deleteItem,
   bestPrice,
   getTransaction,
   addTransaction,
   requestPayment,
   checkPayment,
};
