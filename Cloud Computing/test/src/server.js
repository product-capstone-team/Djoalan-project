const Hapi = require("@hapi/hapi");
const routes = require("./routes");

const init = async () => {
   const server = Hapi.server({
      port: 5000,
      host: "localhost",
      routes: {
         cors: {
            origin: ["*"],
         },
      },
   });

   server.route(routes);

   await server.start();
   console.log(`Server running on %s ${server.info.uri}`);

   await server.register({
      plugin: require("hapi-mongodb"),
      options: {
         url: "mongodb://localhost:27017/user_account",
         settings: {
            useUnifiedTopology: true,
         },
         decorate: true,
      },
   });
};

init();
