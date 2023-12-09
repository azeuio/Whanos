folder('Whanos base images') {
  description('Folder base images jobs.')
}

folder('PROJECTS') {
  description('Folder base images jobs.')
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
        shell('docker build -t whanos-c-standalone -f images/c/Dockerfile.standalone .')
    }
    triggers {
        cron('* * * * *')
    }
}
