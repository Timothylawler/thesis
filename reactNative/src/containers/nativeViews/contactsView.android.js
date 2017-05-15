//import { PropTypes, Component } from 'react';
import React, { Component } from 'react';
import { requireNativeComponent, View } from 'react-native';

var iface = {
	propTypes:{
		...View.propTypes
	}
}

class ContactsView extends Component{
	render(){
		return(
			<ContactsViewManage {...this.props} />
		)
	}
}

ContactsView.propTypes = {
	...View.propTypes
}

var ContactsViewManage = requireNativeComponent('RCTContactsView', ContactsView);

export default ContactsView;
//module.exports = CameraPhotoManager;