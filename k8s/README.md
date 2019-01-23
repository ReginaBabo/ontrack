## Digital Ocean setup

Download the configuration file as `config.yaml`.

> To set this configuration as a default configuration, use:

```bash
export KUBECONFIG=`pwd`/config.yaml
```

Check that everything is in order by running:

```bash
kubectl config view
```

Adapt the user name in `admin.yaml` accordingly.

Create the roles:

```bash
kubectl apply -f admin.yaml
```


Extract the tokens:

```bash
kubectl get secret -n kube-system
# Note the secret for your cluster name
kubectl describe secret  <token> -n kube-system
```

Deploy the dashboard:

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v1.10.1/src/deploy/recommended/kubernetes-dashboard.yaml
```

To access the dashboard:

```bash
kubectl proxy
```

and then navigate to:

http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/

Use the token displayed before to authenticate into the dashboard.

> Reference: https://github.com/kubernetes/dashboard/

## Deploying

    kubectl create namespace test
    kubectl apply --namespace test -f ontrack.yaml

## Deploying on Minikube

To open in a browser:

    minikube service --namespace test ontrack-v2-service

Or to get its URL:

    minikube service --namespace test --url ontrack-v2-service

## Upgrading the version of Ontrack

Change the Ontrack version in `ontrack.yml`.

Redeploy:

    kubectl apply --namespace test -f ontrack.yaml
