terraform {
 backend "gcs" {
   bucket  = "sdoxsee-terraform-admin6"
   prefix  = "terraform/state"
   project = "sdoxsee-terraform-admin6"
 }
}
