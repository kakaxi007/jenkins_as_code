pipelineJob('simple_pipeline_job') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/simple_pipeline_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('groovy_maps') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/groovy_maps.groovy'))
            sandbox()
        }
    }
}

pipelineJob('run_on_slave') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/run_on_slave.groovy'))
            sandbox()
        }
    }
}

pipelineJob('python_no_flush') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/python_no_flush.groovy'))
            sandbox()
        }
    }
}

pipelineJob('python_flush') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/python_flush.groovy'))
            sandbox()
        }
    }
}

pipelineJob('downstream_job') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/downstream_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('parallel_job') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/parallel_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('git_in_pipeline') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/git_in_pipeline'))
            sandbox()
        }
    }
}

pipelineJob('declare_parameter') {
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/declare_parameter'))
            sandbox()
        }
    }
}

pipelineJob('string_parameter_job') {
    parameters {
        stringParam('STRING','default_value','the description')
    }
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/string_parameter_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('choice_parameter_job') {
    parameters {
        stringParam('STRING_PARAM','default_value','the description')
        choiceParam('CHOICE_PARAM', ['1', '2', '3'], 'The choice description')
    }
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/choice_parameter_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('credential_parameter_job') {
    parameters {
        stringParam('STRING','default_value','the description')
        credentialsParam('CREDENTIAL_PARAM_ID') {
            type('any')
            required()
            defaultValue('')
            description('')
        }
    }
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/sample/credential_parameter_job.groovy'))
            sandbox()
        }
    }
}

pipelineJob('discard_old_builds') {
    parameters {
        stringParam('TO_DISCARD_URL','','')
        credentialsParam('ADMIN_CREDENTIAL_ID') {
            type('any')
            required()
            defaultValue('')
            description('')
        }
    }
    definition {
        cps {
            script(readFileFromWorkspace('seed_job/jenkins_admin/discard_old_builds.groovy'))
            sandbox()
        }
    }
}

//def sample_groovys = findFiles(glob: 'seed_job/*/sample_*.groovy')
hudson.FilePath workspace = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
def sample_grooyvs = workspace.child('seed_job/sample').list().findAll { it.name  ==~ /sample.*\.groovy/ }
workspace.child('seed_job').list().each { dir ->
    def groovy_list = dir.list()
    groovy_list.each { sample_groovy ->
        if (sample_groovy.getBaseName() =~ /sample.*/) {
            //println sample_groovy.getBaseName()
            jobName = sample_groovy.getBaseName() 
            pipelineJob("${jobName}") {
                definition {
                    cps {
                        script(readFileFromWorkspace(sample_groovy))
                        sandbox()
                    }
                }
            }  
        }
    }
}


def projects = ['sample','sample1']
for (project_name in projects) {
    sectionedView("${project_name}") {
//create section view based on re
        sections {
            listView {
                name('Prod')
                width('HALF')
                alignment('LEFT')
                jobs {
                    regex("${project_name}"+'_prod.*')
                }
                columns {
                    status()
                    weather()
                    name()
                    lastSuccess()
                    lastFailure()
                }
            }
            listView {
                name('Stage1')
                width('HALF')
                alignment('LEFT')
                jobs {
                    regex("${project_name}" +'_stage.*')
                }
                columns {
                    status()
                    weather()
                    name()
                    lastSuccess()
                    lastFailure()
                }
            }
            listView {
                name('Dev')
                width('HALF')
                alignment('LEFT')
                jobs {
                    regex("${project_name}" +'_dev.*')
                }
                columns {
                    status()
                    weather()
                    name()
                    lastSuccess()
                    lastFailure()
                }
            }
            listView {
                name('Other')
                width('HALF')
                alignment('LEFT')
                jobs {
                    regex("${project_name}" +'_(?!dev|prod|stage).*')
//regular express  not include
                }
                columns {
                    status()
                    weather()
                    name()
                    lastSuccess()
                    lastFailure()
                }
            }
        }
    }
}

sectionedView("JENKINS_ADMIN") {
    sections {
        listView {
            name('ADMIN')
            width('HALF')
            alignment('LEFT')
            jobs {
              selectedJob('discard_old_builds')
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
            }
        }
    }
}
