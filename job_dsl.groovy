pipelineJob('Whanos base images/whanos-python') {
    displayName('whanos-python')
    definition {
        cps {
            script('''
                pipeline {
                    agent any
                    stages {
                        stage('Checkout') {
                            steps {
                                checkout scm
                            }
                        }
                        stage('Build Docker Image') {
                            steps {
                                script {
                                    sh 'docker build -t whanos-python-base -f Dockerfile.base .'
                                }
                            }
                        }
                        stage('Build and Run Project') {
                            steps {
                                script {
                                    sh 'docker run --rm -it whanos-python-base'
                                }
                            }
                        }
                    }
                }
            ''')
        }
    }
    properties {
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: ''))
    }
}



// // Job pour construire toutes les images de base
// pipelineJob('build-all-base-images') {
//     displayName('Build All Base Images')
//     definition {
//         cps {
//             script('''
//                 build 'whanos-haskell'
//                 build 'whanos-python'
//                 // Ajoutez des lignes similaires pour d'autres langages
//             ''')
//         }
//     }
//     properties {
//         buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: ''))
//     }
// }

// // Job link-project
// job('link-project') {
//     displayName('Link Project')
//     parameters {
//         stringParam('projectName', '', 'Nom du projet à lier')
//         // Ajoutez d'autres paramètres si nécessaire
//     }
//     steps {
//         // Ajoutez les étapes nécessaires pour lier le projet à l'infrastructure Whanos
//     }
// }

// // Jobs créés par le job link-project
// pipelineJob('project-1') {
//     displayName('Project 1')
//     triggers {
//         pollSCM('* * * * *') // Vérifie les modifications toutes les minutes
//     }
//     definition {
//         cps {
//             script('''
//                 node {
//                     stage('Checkout') {
//                         // Étape pour récupérer le code source depuis le référentiel du projet 1
//                         // git 'https://votre-repo-git.com/project-1.git'
//                     }

//                     stage('Containerize') {
//                         // Étape pour containeriser l'application selon les spécifications Whanos
//                         // sh 'docker build -t project-1 .'
//                     }

//                     stage('Deploy') {
//                         // Étape pour déployer l'application dans un cluster Kubernetes si applicable
//                         // Utilisez Kubernetes plugin ou kubectl pour le déploiement
//                     }
//                 }
//             ''')
//         }
//     }
//     properties {
//         buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: ''))
//     }
// }

// // Ajoutez des blocs similaires pour d'autres projets créés par le job link-project
