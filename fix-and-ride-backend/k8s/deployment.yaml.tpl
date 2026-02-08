apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-api
  labels: { app: spring-api }
spec:
  replicas: 2
  selector: { matchLabels: { app: spring-api } }
  template:
    metadata: { labels: { app: spring-api } }
    spec:
      containers:
        - name: spring-api
          image: IMAGE_PLACEHOLDER   # CI will replace this
          ports: [ { containerPort: 8080 } ]
          envFrom:
            - configMapRef: { name: spring-api-config }
            - secretRef:    { name: spring-api-secrets }
          readinessProbe:
            httpGet: { path: /actuator/health/readiness, port: 8080 }
            initialDelaySeconds: 10
          livenessProbe:
            httpGet: { path: /actuator/health/liveness,  port: 8080 }
            initialDelaySeconds: 30
          resources:
            requests: { cpu: "100m", memory: "256Mi" }
            limits:   { cpu: "500m", memory: "512Mi" }
