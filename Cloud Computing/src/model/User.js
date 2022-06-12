const mongoose = require("mongoose");
const { Schema } = mongoose;

const userSchema = new Schema({
   nama: String,
   email: String,
   password: String,
   isBusinessAcc: {
      type: Boolean,
      default: false,
   },
   storeName: {
      type: String,
      default: null,
   },
   company: {
      type: String,
      default: null,
   },
   storeLocation: {
      lat: {
         type: Number,
         default: 0.0,
      },
      lon: {
         type: Number,
         defaul: 0.0,
      },
   },
});

const User = mongoose.model("User", userSchema);
module.exports = User;
