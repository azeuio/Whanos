FROM myjenkins-blueocean:2.426.1-1
USER root

COPY jenkins.yml /var/jenkins_home/jenkins.yml
COPY job_dsl.groovy /usr/local/job_dsl.groovy
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

# Copy scripts
COPY ./find_lang /usr/local/bin/find_lang

# Copy images
COPY ./images /usr/local/images

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
ENV CASC_JENKINS_CONFIG /var/jenkins_home/jenkins.yml

RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

ENV ADMIN_PASSWORD aaa
