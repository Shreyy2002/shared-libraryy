class Wrapper implements Serializable {
    def steps

    Wrapper(steps) {
        this.steps = steps
    }

    def init(String dir = 'terraform') {
        steps.echo "Terraform Init"
        steps.dir(dir) {
            steps.sh 'terraform init'
        }
    }

    def validate(String dir = 'terraform') {
        steps.echo "Terraform Validate"
        steps.dir(dir) {
            steps.sh 'terraform validate'
        }
    }

    def plan(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "Terraform Plan"
        steps.dir(dir) {
            steps.sh "terraform plan -var-file=${varFile}"
        }
    }

    def apply(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "Terraform Apply"
        steps.dir(dir) {
            steps.sh "terraform apply -auto-approve -var-file=${varFile}"
        }
    }

    def destroy(String dir = 'terraform', String varFile = 'terraform.tfvars') {
        steps.echo "Terraform Destroy"
        steps.dir(dir) {
            steps.sh "terraform destroy -auto-approve -var-file=${varFile}"
        }
    }
}
