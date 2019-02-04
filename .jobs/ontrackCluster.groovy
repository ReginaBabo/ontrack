// Only for release branches
// FIXME K8S Cleanup
if (BRANCH_NAME ==~ /release\/.*|experimental\/k8s/) {

    // Jenkins-friendly name
    String sanitizedBranch = (BRANCH_NAME as String).replaceAll(/[^A-Za-z0-9._]/, '-')

    // Makes sure the branch folder does exist
    folder("self-service/ontrack/$sanitizedBranch")

    // Loading the pipeline script, replacing the branch name when needed
    String pipelineScript = readFileFromWorkspace('.jobs/pipelines/ontrackCluster.groovy')
    pipelineScript = pipelineScript.replaceAll(/@BRANCH_NAME@/, BRANCH_NAME as String)

    // Creating the job
    pipelineJob("self-service/ontrack/$sanitizedBranch/ontrackCluster") {
        description """
            This job creates a cluster and deploys Ontrack in it.
        """
        definition {
            cps {
                script(pipelineScript)
                sandbox()
            }
        }
    }
}
