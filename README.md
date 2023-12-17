# WHANOS

## How to use

### Requirements

- docker
- ansible
- a google artifactory repository
- jenkins

### Steps

1. Create an `inventory.ini` file, that contains the definition of the `localhost` group
2. Set up the jenkins server by running

```bash
ansible-playbook -i inventory.ini playbook.yaml
```

3. Now go to \<ip-address-used-in-inventory.ini>:8080 and run the `link-project` job with the parameters you want.

4. TODO: Now your projects has been deployed in a docker container and will update every time you push
