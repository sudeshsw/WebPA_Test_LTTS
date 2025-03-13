#################################################
# File: config.py
# Purpose: Holds the config parameters required for the project
# Owner: SYS
# Copyrights (c) Comcast
# Notes:
# Modifications: Created by SYS on 06-Mar-2025
#################################################

BASE_URL = "http://14.142.150.124"

TR1U1DM_PORT = 6100
TALARIA_PORT = 6200
PASS_KEY = "Basic dXNlcjpwYXNz"

rdkb_headings = ["MAC", "SW-Ver", "SSID1", "SSID2", "Model"]
Conn_device_heading = ["Device", "IP-Address", "Status", "Conn-Type"]

HTTP_HEADERS = {'Authorization': 'Basic dXNlcjpwYXNz'}

DEVICE_CMDS_URL = {
        "rdkb_dev_cmd" : "/api/v2/devices",
        "rdkb_mac_cmd":"/api/v2/device/mac:",
        }

TR181_CMDS = {
        "Model" : "Device.DeviceInfo.ModelName",
        "MANUFACTURER" : "Device.DeviceInfo.Manufacturer",
        "SW_Ver" : "Device.DeviceInfo.SoftwareVersion",
        "SSID1" : "Device.WiFi.SSID.10001.SSID",
        "SSID1_PASS" : "Device.WiFi.AccessPoint.10001.Security.X_COMCAST-COM_KeyPassphrase",
        "SSID2" : "Device.WiFi.SSID.10101.SSID",
        "SSID2_PASS" : "Device.WiFi.AccessPoint.10101.Security.X_COMCAST-COM_KeyPassphrase",
        "CONN_DEV_DATA":"Device.Hosts.Host.",
        }

SSID1_NAME = 'Device.WiFi.SSID.10001.SSID'
SSID2_NAME = 'Device.WiFi.SSID.10101.SSID'

SET_PARRAMS = {"dataType": 0}
