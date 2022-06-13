// Get all movies
const getAllUser = async (req, h) => {
   const offset = Number(req.query.offset) || 0;
   const user = await req.mongo.db.collection("users").find({}).sort({ metacritic: -1 }).skip(offset).limit(20).toArray();

   // count number of records in database
   const status = await req.mongo.db.collection("users").count();
   console.log(status);

   // check if collection is populated
   if (status !== 0) {
      const response = h.response({
         status: "success",
         message: "user retrieved",
         data: [user],
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      status: "fail",
      message: "no user retrieved",
      data: [],
   });
   response.code(404);
   return response;
};

// Add a new user to the database
const addUser = async (req, h) => {
   const payload = req.payload;
   const user = await req.mongo.db.collection("users").insertOne(payload);

   if (user !== undefined) {
      const response = h.response({
         status: "success",
         message: "user added to database",
         user,
         data: {
            payload,
         },
      });
      response.code(201);
      return response;
   }

   const response = h.response({
      status: "fail",
      message: "something went wrong",
   });
   response.code(404);
   return response;
};

// Get a single user
const getUser = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;
   const user = await req.mongo.db
      .collection("users")
      .findOne(
         { _id: new ObjectID(id) },
         { projection: { userId: 1, nama: 1, email: 1, password: 1, isBusinessAcc: 1, storeName: 1, company: 1, storeLocation: { lat: 1, lon: 1 } } }
      );

   if (user) {
      const response = h.response({
         status: "success",
         message: "user retrieved",
         data: {
            user,
         },
      });
      response.code(200);
      return response;
   }

   const response = h.response({
      status: "fail",
      message: "something went wrong",
   });
   response.code(500);
   return response;
};

// Update the details of a user
const updateUser = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;

   const payload = req.payload;

   // check payload if field exists inside collection
   const checkPayload = await req.mongo.db.collection("users").findOne(payload);
   console.log(checkPayload);

   if (checkPayload) {
      console.log("field exist");

      const status = await req.mongo.db.collection("users").updateOne({ _id: ObjectID(id) }, { $set: payload });
      console.log(payload);
      console.log(status);

      if (status) {
         const response = h.response({
            status: "success",
            message: "user updated",
            data: {
               payload,
            },
         });
         response.code(201);
         return response;
      }
   } else {
      console.log("field does not exist");

      const response = h.response({
         status: "fail",
         message: "field does not exist",
         data: {
            payload,
         },
      });
      response.code(500);
      return response;
   }

   const response = h.response({
      status: "fail",
      message: "someting went wrong",
   });
   response.code(500);
   return response;
};

// delte user based in objectId mongodb
const deleteUser = async (req, h) => {
   const id = req.params.id;
   const ObjectID = req.mongo.ObjectID;

   const status = await req.mongo.db.collection("users").deleteOne({ _id: ObjectID(id) });
   return status;
};

// Check if user exist in collection by name & email
const checkUser = async (req, nama, h) => {
   // const nama = req.body.nama;

   const checkNama = await req.mongo.db.collection("users").findOne({ nama }.toArray());
   console.log(checkNama);

   if (checkNama) {
      console.log("user exist");

      const response = h.response({
         status: "success",
         message: "user retrieved",
         data: {
            checkNama,
         },
      });
      response.code(200);
      return response;
   } else {
      console.log("user does not exit");

      const response = h.response({
         status: "fail",
         message: "user doesnt exist",
      });
      response.code(500);
      return response;
   }
};

module.exports = { getAllUser, addUser, getUser, updateUser, deleteUser, checkUser };
