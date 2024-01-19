@Library('jenkins-pipeline') _ 

 buildMavenPipeline{
    deploymentApplicationName = 'web-apps-cms-service'
    stepPublishToCodeDx = 'false'
	
    snapshotRepo = 'xfunc-mvn-snapshot-local'
    stagingRepo = 'xfunc-mvn-staging-local'
    releaseRepo = 'xfunc-mvn-release-local'
    runXrayScan = 'true'
    failBuildOnScaGateFailure = 'false'
    xrayWatchName = 'xfunc-mvn-repositories-watch'
 }
