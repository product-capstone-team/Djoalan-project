const Joi = require("@hapi/joi");
Joi.objectId = require("joi-objectid")(Joi);

const { getAllUser, addUser, getUser, updateUser, deleteUser, checkUser } = require("./handler");

const routes = [{}];

module.exports = routes;
