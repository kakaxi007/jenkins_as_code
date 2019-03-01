job('seed_dsl') {
    steps {
        scm {
            git {
                remote {
                    url('https://github.com/kakaxi007/jenkins_as_code.git')
                }
                branch('feature/refactor')
            }
        }
        dsl {
            text(readFileFromWorkspace('seed_job/seed_job.groovy'))
            removeAction('DELETE')
        }
    }
}
