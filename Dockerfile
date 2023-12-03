FROM jenkins/jenkins:lts
USER root
RUN apt-get update && \
    apt-get install -y \
    docker.io \
    && rm -rf /var/lib/apt/lists/*

COPY jenkins.yml /var/jenkins_home/jenkins.yml
COPY job_dsl.groovy /usr/local/job_dsl.groovy

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false

ENV CASC_JENKINS_CONFIG /var/jenkins_home/jenkins.yml

RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

ENV ADMIN_PASSWORD aaa
