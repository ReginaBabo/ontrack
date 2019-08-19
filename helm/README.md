Ontrack Helm Chart
==================

## Vault integration

By default, the Ontrack application uses a local file
storage to store its encryption keys. This does not
scale since in case of pod / node failures, the values
would be lost.

It is therefore recommended to use Vault to store the
encryption keys.

> The Ontrack Helm chart _does not_ include the deployment
of the Vault application.

### Vault deployment

The steps below allow the deployment of Vault using
its [_incubating_ chart](https://github.com/helm/charts/tree/master/incubator/vault).

Start by installing the _incubating_ charts:

    helm repo add incubator http://storage.googleapis.com/kubernetes-charts-incubator

Then install Vault with options predefined for a S3 storage:

    cp vault/values.yaml vault/vault.yaml
    # Edit values in vault/vault.yaml
    helm install incubator/vault \
      --values vault/vault.yaml \
      --name vault-local

> Don't forget to update the values in `vault/vault.yaml`.

Once the service is started, connect to it using:

    kubectl port-forward service/vault-local-vault 8200

and:

    export VAULT_ADDR=http://127.0.0.1:8200
    vault operator init

This renders keys like:

    Unseal Key 1: xxxxxxx
    Unseal Key 2: xxxxx
    Unseal Key 3: xxxxxx
    Unseal Key 4: xxxx
    Unseal Key 5: xxx

    Initial Root Token: xxx

Then, for _each pod_ of Vault, run:

    kubectl port-forward pod/<pod> 8200

and in a different terminal:

    export VAULT_ADDR=http://127.0.0.1:8200
    # Run this command 3 (three) times
    # and provide every time a different unseal key
    vault operator unseal

Vault is now operational.

## Remaining actions

Trello board at https://trello.com/b/0mkdOWnR/ontrack-helm-chart (create a ticket to get access).
