FROM node:16 
WORKDIR /src
COPY package*.json ./
RUN npm install
# Copy local code to the container 
COPY . ./
# Start the service 
CMD ["node", "server.js"]