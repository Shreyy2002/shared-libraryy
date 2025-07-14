package org.cloudninja

class Wrapper implements Serializable {
    def steps

    Wrapper(steps) {
        this.steps = steps
    }

    def init(String dir = 'terraform') {
        steps.echo "🔧 Running: terraform init in '${dir}'"
        steps.dir(dir) {
            steps.sh 'terraform init'
        }
    }

    def validate(String dir = 'terraform') {
        steps.echo "🔎 Running: terraform validate in '${dir}'"
        steps.dir(dir) {
            steps.sh 'terraform validate'
        }
    }

    def plan(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "📝 Running: terraform plan in '${dir}' with '${varFile}'"
        steps.dir(dir) {
            steps.sh """
                export AWS_ACCESS_KEY_ID=${steps.env.AWS_ACCESS_KEY_ID}
                export AWS_SECRET_ACCESS_KEY=${steps.env.AWS_SECRET_ACCESS_KEY}
                terraform plan -var-file=${varFile}
            """
        }
    }

    def apply(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "🚀 Running: terraform apply in '${dir}' with '${varFile}'"
        steps.dir(dir) {
            steps.sh """
                export AWS_ACCESS_KEY_ID=${steps.env.AWS_ACCESS_KEY_ID}
                export AWS_SECRET_ACCESS_KEY=${steps.env.AWS_SECRET_ACCESS_KEY}
                terraform apply -auto-approve -var-file=${varFile}
            """
        }
    }

    def destroy(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "💣 Running: terraform destroy in '${dir}' with '${varFile}'"
        steps.dir(dir) {
            steps.sh """
                export AWS_ACCESS_KEY_ID=${steps.env.AWS_ACCESS_KEY_ID}
                export AWS_SECRET_ACCESS_KEY=${steps.env.AWS_SECRET_ACCESS_KEY}
                terraform destroy -auto-approve -var-file=${varFile}
            """
        }
    }
}
