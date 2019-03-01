#!/usr/bin/env groovy
node('master'){
    stage('CheckOut'){
        checkout([$class: 'GitSCM', branches: [[name: 'feature/refactor']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/kakaxi007/jenkins_as_code.git']]])
    }

    stage("Print") {
        datas = readYaml file: 'seed_job/files/users.yaml'
        echo "HELLO WORLD" 
    }
}
