## Digital Ocean setup

Download the configuration file as `config.yaml`.

Adapt the user name in `admin.yaml` accordingly.

Create the roles:

```bash
kubectl --kubeconfig "config.yaml" apply -f admin.yaml
```

Extract the tokens:

```bash
kubectl --kubeconfig="config.yaml" get secret -n kube-system
# Note the secret for your cluster name
kubectl --kubeconfig="config.yaml" describe secret  <token> -n kube-system
```

Deploy the dashboard:

```bash
kubectl --kubeconfig config.yaml apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v1.10.1/src/deploy/recommended/kubernetes-dashboard.yaml
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
