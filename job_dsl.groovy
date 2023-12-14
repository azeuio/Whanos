folder('Whanos base images') {
  description('Folder base images jobs.')
}

folder('Projects') {
  description('Folder base images jobs.')
}

freeStyleJob('Whanos base images/whanos-befunge') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-befunge-base -f /usr/local/images/befunge/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-c') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-c-base -f /usr/local/images/c/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-javascript') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-javascript-base -f /usr/local/images/javascript/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}


freeStyleJob('Whanos base images/whanos-java') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-java-base -f /usr/local/images/java/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-python') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-python-base -f /usr/local/images/python/Dockerfile.base .')
    }

    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/Build all base images') {
    description('Build Docker Image triggered by GitHub push')

}

freeStyleJob('link-project') {
    description('link project repo')
    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
        stringParam('JOB_NAME', '', 'Git URL of the repository to clone')
    }
    steps {
        dsl {
            text ('''
                    freeStyleJob('Projects/' + JOB_NAME) {
                        description('Job linked')
                        wrappers {
                          preBuildCleanup()
                        }
                        triggers {
                            cron("* * * * *")
                        }
                        steps {
                            scm {
                                git {
                                    remote {
                                        url(GIT_REPOSITORY_URL)
                                        credentials(SSH_KEY_REPO)
                                    }
                                    branches('*/main')
                                }
                            }
                        }
                    }
                ''')
        }
    }
}
