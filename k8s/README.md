## Digital Ocean K8S deployment

### Setup

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

### Dashboard deployment

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

### Ontrack deployment

> Without any further customisation, the deployments mentioned below will use the `default` namespace.

To deploy Ontrack:

```bash
kubectl apply -f ontrack.yaml
```

> You can monitor the progress of the deployment in the dashboard.

#### Upgrade of Ontrack

Change the version of the Ontrack Docker image in `ontrack.yaml` and redeploy:

```bash
kubectl apply -f ontrack.yaml
```

#### Cleanup

To undeploy Ontrack:

```bash
kubectl delete -f ontrack.yaml 
```

### Monitoring

Quick start:

```bash
kubectl apply \
  --filename https://raw.githubusercontent.com/giantswarm/kubernetes-prometheus/master/manifests-all.yaml
```

> See https://github.com/giantswarm/prometheus

By default, the Grafana service is not exposed. For testing purpose, you can expose it using:

```bash
kubectl --namespace monitoring \
    expose service grafana \
    --port=3000 \
    --target-port=3000 \
    --type=LoadBalancer \
    --name=grafana-exposed
```

To remove it after usage:

```bash
kubectl --namespace monitoring delete service grafana-exposed
```
