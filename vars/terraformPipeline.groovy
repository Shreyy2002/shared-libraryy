def call(Map config = [:]) {
    def terraformDir = config.get('terraformDir', 'terraform')
    def tfVarsFile = config.get('tfVarsFile', 'terraform.tfvars')
    def destroyParam = config.get('destroyParam', 'DESTROY_INFRA')
    def credentialsId = config.get('credentialsId', 'aws-keys')

    def tf = new org.cloudninja.Wrapper(this)

    pipeline {
        agent any

        parameters {
            booleanParam(name: destroyParam, defaultValue: false, description: 'Destroy infrastructure?')
        }

        environment {
            TF_IN_AUTOMATION = 'true'
        }

        stages {
            stage('Debug Branch Info') {
                steps {
                    echo "Branch Info - BRANCH_NAME: ${env.BRANCH_NAME}, GIT_BRANCH: ${env.GIT_BRANCH}"
                }
            }

            stage('Terraform Init & Validate') {
                steps {
                    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                        script {
                            tf.init(terraformDir)
                            tf.validate(terraformDir)
                        }
                    }
                }
            }

            stage('Terraform Plan') {
                steps {
                    script {
                        tf.plan(terraformDir, tfVarsFile)
                    }
                }
            }

            stage('Terraform Apply') {
                when {
                    allOf {
                        anyOf {
                            branch 'main'
                            expression { env.GIT_BRANCH == 'main' || env.GIT_BRANCH == 'origin/main' }
                        }
                        not { expression { params[destroyParam] } }
                    }
                }
                steps {
                    script {
                        tf.apply(terraformDir, tfVarsFile)
                    }
                }
            }

            stage('Terraform Destroy') {
                when {
                    expression { return params[destroyParam] }
                }
                steps {
                    script {
                        input message: "Are you sure you want to destroy the infrastructure?"
                        tf.destroy(terraformDir, tfVarsFile)
                    }
                }
            }
        }

        post {
            success {
                echo " Terraform pipeline completed successfully!"
            }
            failure {
                echo " Terraform pipeline failed."
            }
            always {
                echo " Terraform pipeline execution finished."
            }
        }
    }
}
