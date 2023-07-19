@Library('jenkins-pipeline') _ 

 buildMavenPipeline{
    deploymentApplicationName = 'web-apps-cms-service'
    stepPublishToCodeDx = 'false'
    runXrayScan = 'false'
	
    snapshotRepo = 'xfunc-generic-snapshot-local'
    stagingRepo = 'xfunc-generic-staging-local'
    releaseRepo = 'xfunc-generic-release-local'
    runXrayScan = 'true'
    failBuildOnScaGateFailure = 'true'
    xrayWatchName = 'xfunc-generic-repositories-watch'
 }
