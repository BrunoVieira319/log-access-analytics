pipeline {
    agent any
    stages {
    	stage('Run Tests') {
    	    steps {
    	    	sh './gradlew clean test'
        	}
	    }
	    stage('Build the Project') {
	        steps {
	            sh './gradlew clean build -x test'
	        }
	    }
    }
}