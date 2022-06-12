const Joi = require("@hapi/joi");
Joi.objectId = require("joi-objectid")(Joi);

const {
   getAllUser,
   signUp,
   signIn,
   getUserById,
   updateUser,
   deleteUser,
   logout,
   addToCart,
   addItem,
   updateItem,
   deleteItem,
   bestPrice,
   getItemStock,
   addTransaction,
   getTransaction,
   requestPayment,
   checkPayment,
} = require("./handler");

const routes = [
   // ** USER HANDLER ** \\
   {
      method: "GET",
      path: "/users",
      handler: getAllUser,
   },
   {
      method: "POST",
      path: "/sign-up",
      options: {
         auth: false,
         validate: {
            payload: Joi.object({
               nama: Joi.string().required(),
               email: Joi.string().required(),
               password: Joi.string().required(),
               isBusinessAcc: Joi.boolean().default(false),
               storeName: Joi.string().default(null),
               company: Joi.string().default(null),
            }),
            failAction: (request, h, err) => {
               request.log("error", err);
               throw err;
            },
         },
      },
      handler: signUp,
   },
   {
      method: "POST",
      path: "/sign-in",
      options: {
         auth: {
            mode: "try",
         },
      },
      handler: signIn,
   },
   {
      method: "GET",
      path: "/users/{id}",
      handler: getUserById,
   },
   {
      method: "PUT",
      path: "/users/{id}",
      options: {
         validate: {
            payload: Joi.object({
               storeName: Joi.string().required(),
               company: Joi.string().required(),
               storeLocation: Joi.object({
                  lat: Joi.number().required(),
                  lon: Joi.number().required(),
               }),
            }),
            failAction: (request, h, err) => {
               request.log("error", err);
               throw err;
            },
         },
      },
      handler: updateUser,
   },
   {
      method: "DELETE",
      path: "/users/{id}",
      handler: deleteUser,
   },
   {
      method: "GET",
      path: "/logout",
      handler: logout,
   },
   // **ITEMS HANDLER** \\
   {
      method: "GET",
      path: "/itemStock",
      handler: getItemStock,
   },
   {
      method: "POST",
      path: "/addToCart/{id}",
      handler: addToCart,
   },
   {
      method: "POST",
      path: "/add-item",
      options: {
         payload: {
            output: "stream",
            multipart: true,
         },
      },
      handler: addItem,
   },
   {
      method: "PUT",
      path: "/update-item/{id}",
      options: {
         payload: {
            output: "stream",
            multipart: true,
         },
      },
      handler: updateItem,
   },
   {
      method: "DELETE",
      path: "/delete-item/{id}",
      options: {
         validate: {
            params: Joi.object({
               id: Joi.objectId(),
            }),
         },
      },
      handler: deleteItem,
   },
   {
      method: "GET",
      path: "/bestPrice",
      handler: bestPrice,
   },
   // ** TRANSACTION HANDLERS ** \\
   {
      method: "POST",
      path: "/transaction",
      handler: addTransaction,
   },
   {
      method: "GET",
      path: "/transaction/{id}",
      handler: getTransaction,
   },
   {
      method: "POST",
      path: "/payment",
      options: {
         payload: {
            multipart: true,
         },
      },
      handler: requestPayment,
   },
   {
      method: "GET",
      path: "/checkPayment/{id}",
      handler: checkPayment,
   },
   {
      method: "*",
      path: "/{any*}",
      handler: () => "Halaman tidak ditemukan",
   },
];

module.exports = routes;
