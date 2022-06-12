const mongoose = require("mongoose");
const { Schema } = mongoose;

const itemSchema = new Schema({
   productId: {
      type: String,
      required: true,
   },
   name: {
      type: String,
      required: true,
   },
   brand: {
      type: String,
      required: true,
   },
   expiredDate: Date,
   price: Number,
   category: {
      type: String,
      required: true,
   },
   quantity: {
      type: Number,
      required: true,
   },
   imageUrl: {
      type: String,
      required: true,
   },
   companyId: {
      type: Schema.Types.ObjectId,
      ref: "User",
   },
});

const Item = mongoose.model("Item", itemSchema);
module.exports = Item;
