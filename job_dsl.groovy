folder('Whanos base images') {
  description('Folder base images jobs.')
}

folder('PROJECTS') {
  description('Folder base images jobs.')
}

freeStyleJob('Whanos base images/whanos-befunge') {
    description('Build Docker Image triggered by GitHub push')

    wrappers {
      preBuildCleanup()
    }

    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }

    steps {
        scm {
            git {
                remote {
                    url('${GIT_REPOSITORY_URL}')
                    credentials('SSH_KEY_REPO')
                }
                branches('*/main')
            }
        }
        shell('docker build -t whanos-befunge-base -f images/befunge/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-c') {
    description('Build Docker Image triggered by GitHub push')

    wrappers {
      preBuildCleanup()
    }

    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }

    steps {
        scm {
            git {
                remote {
                    url('${GIT_REPOSITORY_URL}')
                    credentials('SSH_KEY_REPO')
                }
                branches('*/main')
            }
        }
        shell('docker build -t whanos-c-base -f images/c/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-javascript') {
    description('Build Docker Image triggered by GitHub push')

    wrappers {
      preBuildCleanup()
    }

    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }

    steps {
        scm {
            git {
                remote {
                    url('${GIT_REPOSITORY_URL}')
                    credentials('SSH_KEY_REPO')
                }
                branches('*/main')
            }
        }
        shell('docker build -t whanos-javascript-base -f images/javascript/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-python') {
    description('Build Docker Image triggered by GitHub push')

    wrappers {
      preBuildCleanup()
    }

    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
    }

    steps {
        scm {
            git {
                remote {
                    url('${GIT_REPOSITORY_URL}')
                    credentials('SSH_KEY_REPO')
                }
                branches('*/main')
            }
        }
        shell('docker build -t whanos-python-base -f images/python/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/Build all base images') {
    description('Build Docker Image triggered by GitHub push')

}