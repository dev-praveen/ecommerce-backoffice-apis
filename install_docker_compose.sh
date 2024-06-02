#!/bin/bash

# Function to check if Docker Compose plugin is installed
is_docker_compose_plugin_installed() {
    if docker compose version &> /dev/null; then
        return 0
    else
        return 1
    fi
}

# Function to install Docker Compose plugin
install_docker_compose_plugin() {
    echo "Updating package database..."
    sudo apt-get update -y

    echo "Installing Docker Compose plugin..."
    sudo apt-get install -y docker-compose-plugin

    echo "Docker Compose plugin installation completed successfully!"
}

# Main script execution
echo "Checking if Docker Compose plugin is installed..."
if is_docker_compose_plugin_installed; then
    echo "Docker Compose plugin is already installed on this system."
else
    echo "Docker Compose plugin is not installed. Installing now..."
    install_docker_compose_plugin
fi

echo "Script execution finished."