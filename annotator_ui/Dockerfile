FROM node:19.7

WORKDIR /app

COPY package.json yarn.lock /app/

RUN cd /app && yarn install --pure-lockfile

COPY . /app

EXPOSE 3000