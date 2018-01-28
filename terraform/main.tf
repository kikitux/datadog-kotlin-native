variable datadog_api_key {}
variable datadog_app_key {}

# Configure the Datadog provider
provider "datadog" {
  api_key = "${var.datadog_api_key}"
  app_key = "${var.datadog_app_key}"
}

# Create a new Datadog timeboard
resource "datadog_timeboard" "web" {
  title       = "Sample Timeboard for dummy web app (created via Terraform)"
  description = "created using the Datadog provider in Terraform"
  read_only   = true

  graph {
    title = "Web hits"
    viz   = "timeseries"

    request {
      q    = "sum:web.hits{*} by {myapp}"
      type = "bars"
    }

    events = ["web.backend"]
  }

  graph {
    title = "Web unique"
    viz   = "timeseries"

    request {
      q    = "sum:web.unique{*} by {myapp}"
      type = "bars"
    }

    request {
      q    = "sum:web.hits{*}"
      type = "line"
    }
  }
}
