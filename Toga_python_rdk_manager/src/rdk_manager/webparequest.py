###########################################################
# File: webparequest.py
# Purpose: Manage GET, PATCH requests for WebPA from external Application.
# Owner: SYS
# Copy rights: (c) Comcast
# Notes: httpx library is used for REST request mangement
# Modifications: Created by SYS on 06-Mar-2025
###########################################################

# import modules
import httpx
from rdk_manager.config import *


######################################################
# Class: rdkbRequestHandle
# All methods handle HTTPX requests (GET and PATCH)
#
######################################################
class rdkbRequestHandle(object):
    """
    Class for RDKB webpa request handler
    """

    def __init__(self):
        pass
    
    #############################################################
    # Method: get_httpx_Params()
    # Purpose: Static Method for handling GET request 
    # Note: -
    #############################################################  
    @staticmethod
    def get_httpx_Params(dev_cmd, port, mac="", params={}):
        """
        Get json response for get params
        """
        ret_data = {}
        # form url
        if mac:
            dev_cmd_url_part = DEVICE_CMDS_URL.get(dev_cmd, "")+mac+"/config"
        else:
            dev_cmd_url_part = DEVICE_CMDS_URL.get(dev_cmd, "")
        	
        url = "{}:{}{}".format(BASE_URL, port, dev_cmd_url_part)
        print("URL :{} Params: {} ".format(url, params)) 
        
        
        res = httpx.get(url, headers=HTTP_HEADERS, params=params)
        
        
        # print("GET Url: {} and Response : {}".format(res.url, res))
        if res.status_code <300:
            #ret_data = json.dumps(res.json(), indent=4, sort_keys=True)
            ret_data = res.json()
        return ret_data
    

    #############################################################
    # Method: get_httpx_Params()
    # Purpose: Static Method for handling SET PATCH request 
    # Note: -
    #############################################################  
    @staticmethod
    def set_httpx_params(dev_cmd, port, mac, param, value):
        """
        Method to update the parmeters on webpa server
        """
        
        ret_data = {}
        dev_cmd_url_part = DEVICE_CMDS_URL.get(dev_cmd, "")+mac+"/config"
        	
        url = "{}:{}{}".format(BASE_URL, port, dev_cmd_url_part)

        data = '{"parameters": [ {"dataType": 0, "name": "'+param+'", "value": "'+value+'"}]}'
        
        print("Data:", data)
        
        
        res = httpx.request(method="PATCH", url=url, headers=HTTP_HEADERS, data=data,)
        
        print("PATCH Url: {} and Response : {}".format(res.url, res))
        
        if res.status_code <300:
            #ret_data = json.dumps(res.json(), indent=4, sort_keys=True)
            ret_data = res.json()
        return ret_data


