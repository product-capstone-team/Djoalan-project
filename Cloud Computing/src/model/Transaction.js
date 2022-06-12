const mongoose = require("mongoose");
const { Schema } = mongoose;

const transactionSchema = new Schema({
   listOfItems: {
      type: [Schema.Types.ObjectId],
      ref: "Item",
   },
   total: Number,
   date: {
      type: Date,
      default: Date.now,
   },
   userId: {
      type: Schema.Types.ObjectId,
      ref: "User",
   },
});

const Transaction = mongoose.model("Transaction", transactionSchema);
module.exports = Transaction;
