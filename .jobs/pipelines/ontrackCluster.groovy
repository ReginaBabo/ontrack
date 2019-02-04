@Library("digital-ocean-jenkins-library@master") _

String branchName = ""

pipeline {
    agent any

    environment {
        BRANCH_NAME = '@BRANCH_NAME@'
    }

    stages {
        stage("K8S Cluster") {
            steps {
                script {
                    branchName = ontrackBranchName(BRANCH_NAME)
                }
                withDigitalOceanK8SCluster(
                        logging: true,
                        verbose: true,
                        destroy: false,
                        credentials: "DO_NEMEROSA_JENKINS2_BUILD",
                        name: "${branchName}",
                        region: "ams3",
                        version: "1.13.1-do.2",
                        tags: [
                                "ontrack",
                                "ontrack:branch:$branchName",
                        ],
                        pools: [[
                                        name : "ontrack-${branchName}-pool",
                                        count: 2,
                                        size : "s-1vcpu-2gb"
                                ]]
                ) { cluster ->
                    echo "K8S ID = ${cluster.id}"
                    withDeployment(file: "k8s/ontrack.yaml", delete: false) {
                        waitForDigitalOceanLoadBalancer(
                                service: "ontrack-web-service",
                                outputVariable: "ONTRACK_IP",
                                logging: true,
                        )

                        echo "Ontrack IP = ${env.ONTRACK_IP}"

                    }
                }
            }
        }
    }
}