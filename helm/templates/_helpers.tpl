{{/*
Expand the name of the chart.
*/}}
{{- define "ontrack.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Allow the release namespace to be overridden for multi-namespace deployments in combined charts.
*/}}
{{- define "ontrack.namespace" -}}
  {{- if .Values.namespaceOverride -}}
    {{- .Values.namespaceOverride -}}
  {{- else -}}
    {{- .Release.Namespace -}}
  {{- end -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "ontrack.fullname" -}}
{{- if .Values.app.fullnameOverride -}}
{{- .Values.app.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.app.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
  Creates the prefix to use in Vault secrets.
*/}}
{{- define "ontrack.vault.prefix" -}}
{{- if .Values.container.env.vault.prefix -}}
{{- .Values.container.env.vault.prefix -}}
{{- else -}}
{{- printf "secret/%s/key" .Release.Name -}}
{{- end -}}
{{- end -}}
