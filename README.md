# WHANOS

## How to use standalone dockerfiles
This exemple uses python, but you can use the Dockerfile you want among the `images/<language>/Dockerfile.standalone` files 

go into your whanos compatible directory and run the command below.
> Don't forget to replace `<PATH>` by the path needed to reach the root of this repository
```bash
docker build -t whanos-python -f <PATH>/images/python/Dockerfile.standalone .
```