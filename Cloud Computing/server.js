const Hapi = require("@hapi/hapi");
const routes = require("./src/routes");

const init = async () => {
   const server = Hapi.server({
      port: 8080,
      routes: {
         cors: {
            origin: ["*"],
         },
      },
   });

   // Pluggins
   await server.register([
      {
         plugin: require("hapi-mongodb"),
         options: {
            url: "mongodb://34.124.224.131:27017/djoalan?directConnection=true",
            settings: {
               useUnifiedTopology: true,
            },
            decorate: true,
         },
      },
      {
         plugin: require("@hapi/cookie"),
      },
   ]);

   // Auth strategy for cookie
   server.auth.strategy("login", "cookie", {
      cookie: {
         name: "session",
         password: "!wsYhFA*C2U6nz=Bu^%A@^F#SF3&kSR6",
         isSecure: false,
      },
      validateFunc: async (req, session) => {
         if ((session.email, session.password)) {
            return { valid: true };
         } else {
            return { valid: false };
         }
      },
   });

   server.auth.default("login");

   server.route(routes);

   await server.start();
   console.log(`Server running on %s ${server.info.uri}`);
};
init().catch(console.error);
