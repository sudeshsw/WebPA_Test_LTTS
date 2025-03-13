###########################################################
# File: app.py
# Purpose: RDKB-Manager main control.
# Owner: SYS
# Copy rights: (c) Comcast
# Notes: httpx library is used for REST request mangement
# Modifications: Created by SYS on 06-Mar-2025
###########################################################
"""
Use WebPa to configure rdkb devices
"""

from rdk_manager.config import *
import rdk_manager.webparequest as webpareq
import toga
from toga.style import Pack
from toga.style.pack import BOLD
from toga.style.pack import COLUMN, ROW
from toga.validators import MinLength
from toga.constants import BLUE, CENTER, COLUMN, GREEN, RED, ROW, WHITE, YELLOW, GREY
from toga.sources import ListSource
import time, re



######################################################
# Class: rdk_manager
# All methods for Mobile Application
#
######################################################
class rdk_manager(toga.App):
    """
    class for RDK manager
    Holds all the methods for varios events on the application
    
    """
    
    #############################################################
    # Method: save_btn_handler()
    # Purpose: Handler for save button to save SSID Params.
    # Note: -
    #############################################################  
    def save_btn_handler(self, widget):
        """
        Operation on save button
        curl -X PATCH http://14.142.150.124:6100/api/v2/device/mac:2ccf67a1bf7f/config -d '{"parameters": [ {"dataType": 0, "name": "Device.WiFi.SSID.10001.SSID", "value": "Testing"}]}' -H 'Authorization: Basic dXNlcjpwYXNz'
        """
        # Save the SSID and Pass for selected RDK-B mac
        port = TR1U1DM_PORT
        mac = self.selected_mac_row
        params = {}
        params.update(SET_PARRAMS)
        set_params = []
        set_params.append({"name":SSID1_NAME, "value":self.ssid1_input.value})
        set_params.append({"name":SSID2_NAME, "value":self.ssid2_input.value})
        
        print("Setting Params {} for mac: {}".format(set_params, self.selected_mac_row))
        
        for p in set_params:
            res = webpareq.rdkbRequestHandle.set_httpx_params(dev_cmd='rdkb_mac_cmd', port=port, mac=mac, param=p.get('name', ""), value=p.get('value', ""))
            if res:
                print("Response :{}".format(res))   
        
    
    #############################################################
    # Method: refresh_btn_handler()
    # Purpose: Handler for refresh button
    # Note: -
    #############################################################  
    def refresh_btn_handler(self, input):
        """
        Handler for refresh button press to read the current RDK devices. 
        """
        port = TALARIA_PORT
        self.mac_list = []
        self.rdkb_data = []
        print("Fetching RDK devices using updated URL: {}:{}/api/v2/devices".format(self.name_input.value, port))
        
        res = webpareq.rdkbRequestHandle.get_httpx_Params(dev_cmd='rdkb_dev_cmd', port=port)
        if res:
            for elements in res.get('devices', []):
                mac = elements.get('id').split(":")[-1] if ('mac' in elements.get('id', "")) else 'NA' 
                self.mac_list.append(mac) 
        # print("Response: {}".format(res))
        
        print("Mac List : {}".format(self.mac_list))
        data_to_update = {}
        rdkdata = []
        for mac in self.mac_list:
            res = self.get_params_for_rdkb_mac(mac)
            # print("===== Params for MAC {} : {}".format(mac, res))
            data_to_update = {'MAC':mac}
            data_to_update.update(res[mac])
            ssid1 = data_to_update.get('SSID1', 'NA')
            ssid1_pass = data_to_update.get('SSID1_PASS', 'NA')
            ssid2 = data_to_update.get('SSID2', 'NA')
            ssid2_pass = data_to_update.get('SSID2_PASS', 'NA')
            
            # print(data_to_update)
            self.ssid_pass={mac:{'SSID1':ssid1, 'SSID1_PASS':ssid1_pass, 'SSID2':ssid2, 'SSID_PASS': ssid2_pass}}
            rdkdata.append(data_to_update)
            # print("Rdk data: ", data_to_update, rdkdata)
            #data_to_update = {}
            
        #print("====== RDKB Data =====")
        #print(rdkdata)
        self.rdkb_table.data.clear()
        table_data = []
        for data in rdkdata:
            temp_lst = []
            for key in rdkb_headings:
                temp_lst.append(data.get(key, "-"))
            table_data.append(temp_lst)
        # print("Table data:", table_data)
        for d in table_data:
            self.rdkb_table.data.append(d)
        
        return res

    #############################################################
    # Method: get_params_for_rdkb_mac()
    # Purpose: Get RDK MAC details from WebPA for Connected RDKB devices.
    # Note: -
    #############################################################  
    def get_params_for_rdkb_mac(self, mac):
        """
        Handler for fecting R. 
        """
        port = TR1U1DM_PORT
        self.rdkb_data_mac = {mac:{}}

        print("Fetching RDK device parameters")
        for cmd in TR181_CMDS:
            params={"names":TR181_CMDS[cmd]}        
            res = webpareq.rdkbRequestHandle.get_httpx_Params(dev_cmd='rdkb_mac_cmd', mac=mac, port=port, params=params)
            if res:
                temp_dict = {cmd:res['parameters'][0].get('value', 'NA')}
                self.rdkb_data_mac[mac].update(temp_dict)
            time.sleep(0.15)
        
        return self.rdkb_data_mac

    #############################################################
    # Method: on_select_table_row()
    # Purpose: Operations on RDK-B MAC Row
    # Note: This is to update SSID params in text box and 
    #       enable settings button. 
    #############################################################  
    def on_select_table_row(self, table):
        
        conn_data = {}
        data_elem = ['IPAddress', 'Layer1Interface', 'HostName', 'Active']
        try:
            self.selected_mac_row = table.selection.mac
            conn_dev_data = self.rdkb_data_mac[self.selected_mac_row].get("CONN_DEV_DATA")
            
            # get last element in the list
            last_ele = conn_dev_data[-1].get('name', '0')
            num_dev = int(re.findall(r'\d+', last_ele)[0])
            data_len = len(conn_dev_data)
            print("Num dev: {}, list len: {}".format(num_dev, data_len))
            
            data_list = []
            line_count = 0
            for i in range(1, num_dev):
                temp_dict = {}
                for ele in data_elem:
            	    conn_dev_data = conn_dev_data[line_count:]
            	    line_count = 0
            	    for data in conn_dev_data:
            	        line_count += 1
            	        #data_len-=1 
            	        search_str = "Device.Hosts.Host.{}.{}".format(i, ele)
            	        data_line =  data.get('name')
            	        # print("Search Str: {}, data line: {} data_lebn: {} ".format(search_str, data_line, line_count))
            	        if search_str in data_line:
            	    	    temp_dict[ele] = data.get('value')
            	    	    # print("Got param: {} and value: {} Line count: {}".format(data.get('name'), data.get('value'), line_count))
            	    	    break
                data_list.append(temp_dict)    	    
            #print("Data List : {}".format(data_list))    	    
            
            #print("========= Connected  dev data for mac: {} =============".format(self.selected_mac_row))
            
            # map captured data to Connected devices heading
            new_data_list = []
            for data in data_list:
                temp_dict = {}
                temp_dict['Device'] = data['HostName']
                temp_dict['IP-Address'] = data['IPAddress']
                temp_dict['Conn-Type'] = data['Layer1Interface']
                temp_dict['Status'] = 'Active' if data['Active'] == 'true' else 'In-Active'
                new_data_list.append(temp_dict)
            
            print("========= Connected  dev data for mac: {} \n {}=============".format(self.selected_mac_row, new_data_list))
            
            # Read rdkb connected device data and update the same to table
            self.rdkb_dc_table.data.clear()
            table_data = []
            for data in new_data_list:
                temp_lst = []
                for key in Conn_device_heading:
                    temp_lst.append(data.get(key, "-"))
                table_data.append(temp_lst)
            
            # print("Table data:", table_data)
            for d in table_data:
                self.rdkb_dc_table.data.append(d)
            
            self.set_button.enabled = True
            
            self.ssid1_input.value = self.ssid_pass.get(self.selected_mac_row).get('SSID1')
            self.ssid2_input.value = self.ssid_pass.get(self.selected_mac_row).get('SSID2')
            
        except Exception as e:
            print("Exception row select: {}".format(e))
            
    #############################################################
    # Method: on_table_dble_click()
    # Purpose: Handler for double click on mac row
    # Note: This is to edit SSID params. 
    #############################################################    
    def on_table_dble_click(self, table, row):
        try:
            print("Double Clicked row for Mac: {}".format(row.mac))
            self.dble_clik_mac_row = row.mac
            # self.ssid_password_window(row.mac)
        except Exception as e:
            print("Exception double click: {}".format(e))
                

    #############################################################
    # Method: settings_btn_handler()
    # Purpose: Handler for handling settings button click
    # Note: Settings button click will enable ssid input text box 
    #############################################################
    def settings_btn_handler(self, widget):
        """
        Handler for settings button pressed for any RDK-B device
        Mainly to change SSID/Password
        """
        mac = self.selected_mac_row
        self.save_button.enabled = True
        self.ssid1_input.readonly = False
        self.ssid2_input.readonly = False
        
 
    #############################################################
    # Method: get_webpa_box()
    # Purpose: Create webPA box to hold WbPA URL, Refresh button
    #
    #############################################################
    def get_webpa_box(self):
        """
        Create webpa Box
        """
        # Components for webPA box
        name_label = toga.Label("WebPA URL: ", style=Pack(padding=(15, 5), font_weight=BOLD))
        self.name_input = toga.TextInput(style=Pack(flex=1, padding=(5,5), padding_top=6, font_size=9, font_weight=BOLD), value=BASE_URL, on_change=self.refresh_btn_handler)
        
        # Button with refresh icon
        refresh_button = toga.Button(icon=toga.Icon("resources/refresh1.png"), on_press=self.refresh_btn_handler, style=Pack(width=70, padding_right=5, padding_top=3, padding_bottom=2))
       
        # Webpa box
        webpa_box = toga.Box(style=Pack(direction=ROW, padding=(2,2), font_family='monospace',
                font_size=8,
                font_style='normal',),
        children=[
                name_label,
                self.name_input,
                refresh_button,
                ],
         )
        #webpa_box.style.border_width = 4
        #webpa_box.style.border_color = "black"
        webpa_box.style.background_color = GREY
        return webpa_box

        
    #############################################################
    # Method: get_data_table_box()
    # Purpose: Create Box for table data Reuse same method for 
    # RDK-B device and Table for Connected devices.
    #
    #############################################################
    def get_data_table_box(self, table_name, ttable, table_col_header, table_data_lst):
        """
        Create box for list of data dictionaries.
        """
        # Set table name as Label to show.
        label = toga.Label(table_name, style=Pack(padding=(10, 10), font_weight=BOLD))

        # Button with refresh icon
        
        # format table data aligned to header
        table_data = []
        for data in table_data_lst:
            temp_lst = []
            for key in table_col_header:
                temp_lst.append(data.get(key, "-"))
            table_data.append(temp_lst)
        # print("Table data:", table_data)
        
        ttable.data = table_data
        
        #create toga table view
        # Outermost box
        table_box = toga.Box(
            children=[label, ttable],
            style=Pack(
                flex=1,
                direction=COLUMN,
                padding=5,
            ),
        )
        
        table_box.style.background_color = WHITE
        
        return table_box
        
        
    #############################################################
    # Method: startup
    # Purpose: Constructor and first method to launch main window
    #
    #############################################################
    def startup(self):
        """
        Construct and show the  application.
        Main function to launch the main window of the application
        """

        # Globals used across project
        # RDKB data for mac
        self.rdkb_data_mac = {}
        self.ssid_pass={}
        self.dble_clik_mac_row = ''
        self.set_params = []
        
        # start wih creation of Main box 
        main_box = toga.Box(style=Pack(direction=COLUMN))
        
        # BOX#1 Header box at the top with RDK Manager logo
        header_box = toga.Box(style=Pack(direction=ROW))
        my_image = toga.Image("resources/rdk2.png")
        view = toga.ImageView(my_image, style=Pack(direction=ROW, padding=(15, 15)))
        
        # Settings and Save buttons for updating the SSIDs and related params.
        # Keep both buttons disabled enable only when RDKB device is detected and selected for edit
        self.set_button = toga.Button(icon=toga.Icon("resources/settings.png"), on_press=self.settings_btn_handler, style=Pack(direction=ROW, width=70, padding_right=5))
        self.set_button.enabled = False
        self.save_button = toga.Button(icon=toga.Icon("resources/save.png"), on_press=self.save_btn_handler, style=Pack(direction=ROW, width=70, padding_right=5))
        self.save_button.enabled = False
        
        # Add Image view, setting and save buttons at header box.
        header_box.add(view)
        header_box.add(self.set_button)
        header_box.add(self.save_button)
        
        # BOX#2 Box for SSID details to edit by default it's read only, user can edit only when settings button is pressed.
        self.ssid_update_box = toga.Box(style=Pack(direction=ROW))
        ssid_label = toga.Label("SSIDs: ", style=Pack(padding=(10, 5), font_weight=BOLD))
        self.ssid1_input = toga.TextInput(style=Pack(flex=1, padding=(5,5), font_size=9,), value='', readonly=True,)
        self.ssid2_input = toga.TextInput(style=Pack(flex=1, padding=(5,5), font_size=9,), value='', readonly=True,)
        self.ssid_update_box.add(ssid_label)
        self.ssid_update_box.add(self.ssid1_input)
        self.ssid_update_box.add(self.ssid2_input)
        
        # BOX#3 Box for Web PA server details wnd a refresh button to fetch all the details from RDK-B devices. 
        webpa_box = self.get_webpa_box()
	
	# BOX#4 Box for table data for RDKB devices
        rdkb_data = [{"MAC": 'MAC1', "SW-Ver": None, "SSID1": None, "SSID2": None, "Model": "RPI-1"}]
        # create toga table view for RDK B devices
        self.rdkb_table = toga.Table(
            headings=rdkb_headings,
            style=Pack(
                flex=1,
                padding_right=5,
                padding_left=5,
                padding_bottom=5,
                font_family='monospace',
                font_size=8,
                font_style='normal',
                height=100,
                ),
            multiple_select=False,
            on_select=self.on_select_table_row,
            on_activate=self.on_table_dble_click,
            )
        rdkb_box = self.get_data_table_box("RDK-B Devices:", self.rdkb_table, rdkb_headings, rdkb_data)
	
        # BOX#5 Box for table data for RDKB connected devices
        rdkb_dc_data = [{"Device": None, "IP-Address": None, "Conn-Type": None, "SSID": None, "Status":'NA'}]
        
        # create toga table view for connected devices
        self.rdkb_dc_table = toga.Table(
            headings=Conn_device_heading,
            data=rdkb_dc_data,
            style=Pack(
                flex=1,
                padding_right=5,
                padding_left=5,
                padding_bottom=5,
                font_family='monospace',
                font_size=8,
                font_style='italic',
                #height=120,
                ),
            multiple_select=False,
            on_select=self.on_select_table_row,
            )
        
        rdkb_dc_box = self.get_data_table_box("RDK-B Connected Devices:", self.rdkb_dc_table, Conn_device_heading, rdkb_dc_data)
        
        # Just hackathon button at the bottom
        button = toga.Button(
            "RDK Hackathon 2025!!!",
            on_press=self.get_details,
            style=Pack(direction=ROW, color=BLUE, background_color=YELLOW, padding=5),
        )
        
        # Place all component Boxes in Main box.
        main_box.add(header_box)
        main_box.add(self.ssid_update_box)
        main_box.add(webpa_box)
        main_box.add(rdkb_box)
        #container1 = toga.ScrollContainer(style=Pack(direction=ROW,), horizontal=True, vertical=True, content=rdkb_box)
        #container2 = toga.ScrollContainer(style=Pack(direction=ROW,), horizontal=True, vertical=True, content=rdkb_dc_box)
        main_box.add(rdkb_dc_box)
        #main_box.add(container1)
        #main_box.add(container2)
        main_box.add(button)
        
        # Place Main Box in Mian window
        self.main_window = toga.MainWindow(title=self.formal_name, size=(400, 600), resizable=True,)
        self.main_window.content = main_box
        self.main_window.show()
 
    # Sample button handler for bottom butoon click
    def get_details(self, widget):
        print(f"Hello, Testing webPA Server with {self.name_input.value}")
    

######################################################
# Method Main 
# to launch rdk_manager app
#
######################################################  
def main():
    return rdk_manager()
    
  
