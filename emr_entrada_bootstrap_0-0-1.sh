#!/usr/bin/env bash

# This bootstrap script is made to install a modified version of ENTRADA
# on the Amazon Web Services service, Elastic Map Reduce v5.20.0.
#
# ENTRADA, a big data platform for network data analytics
# Copyright (C) 2016 SIDN [https://www.sidn.nl]
#
# some other text idk
#

config=$1

yum install -y git

#download the package
cd /home/hadoop
git clone https://EMR_CodeCommit-at-845534697080:R9GGhQEz2rcrMfYFmmX9TSTlNbbZnNKHzMBeiXb1OUs=@git-codecommit.eu-west-1.amazonaws.com/v1/repos/entrada-0.1.0-internetstiftelsen-0.1 entrada
aws s3 cp $config entrada/scripts/run/config.sh
ln -s entrada entrada-latest

#create directories for processing
mkdir ./pcap
mkdir ./pcap/processing
mkdir ./pcap/processed

mkdir ./tmp

chown -R hadoop:hadoop ./
chmod -R 700 ./

sh ./entrada-latest/scripts/install/create_s3External_tables.sh

#create log dir and set up logrotate
mkdir -p /var/log/entrada
chown hadoop:hadoop /var/log/entrada
cat > /etc/logrotate.d/entrada << EOF
/var/log/entrada/*.log {
  size 10k
  daily
  maxage 10
  compress
  missingok
}
EOF

# for some reason below code did not work when giving a relative path (for the source), wont debug further for now since absolute path works
source /home/hadoop/entrada-latest/scripts/run/config.sh && sh ./entrada-latest/scripts/run/run_update_geo_ip_db.sh

exit 0
