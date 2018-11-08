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
# Note the secret for your cluster
kubectl --kubeconfig="config.yaml" describe secret  <token> -n kube-system
```

Deploy the dashboard:

```bash
kubectl --kubeconfig config.yaml apply -f https://raw.githubusercontent.com/kubernetes/dashboard/master/src/deploy/recommended/kubernetes-dashboard.yaml
```

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
