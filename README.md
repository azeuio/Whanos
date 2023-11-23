# WHANOS

## How to use the whanos images
These exemples use python, but you can use the Dockerfile you want among the `images/<language>/Dockerfile.<standalone|base>` files 
> Don't forget to replace `<PATH>` by the path needed to reach the root of this repository

### How to use standalone dockerfiles
go into your whanos compatible directory and run the command below.
```bash
docker build -t whanos-python-standalone -f <PATH>/images/python/Dockerfile.standalone .
```

### How to use standalone dockerfiles
create the whanos base image with
```bash
docker build -t  whanos-python - < <PATH>/images/python/Dockerfile.base
```
then go into your whanos compatible directory and create a Dockerfile that starts with
```Dockerfile
FROM whanos-python
```
and then installs the dependencies, compile and launch your app the way you want.
