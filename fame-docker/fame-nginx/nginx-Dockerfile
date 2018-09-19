FROM nginx:1.15.3-alpine

MAINTAINER zzzzbw "zzzzbw@gmail.com"

COPY ./fame-docker/fame-nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]