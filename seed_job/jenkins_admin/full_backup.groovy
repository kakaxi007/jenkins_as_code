#!/usr/bin/env groovy
node('master'){
    stage("clone") {
        git url: 'https://github.com/kakaxi007/jenkins_test.git'
        echo 'Cloning...'
    }
    stage('Backup') {
            sh '/usr/bin/python python_scripts/full_backup.py'
    }
}
