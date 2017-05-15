import React, { Component } from 'react';

import {
	ListView,
	View,
	StyleSheet,
	Button,
	Text
} from 'react-native';

//	Redux
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

//	Redux actions
import * as LongListActions from '../actions/longListActions';

import DefaultText from '../components/defaultText';

//	Get list
var jsonlist = require('../assets/longlist');
//	Styles
import containerStyle from '../styles/container';




class LongList extends Component {

	constructor(props){
		super(props);
	}

	

	componentWillMount(){
		//	Wonder if this should be moved to background thread...
		if(this.props.rows.items === undefined || this.props.rows.length == 0){
			this.props.actions.fill(jsonlist);
		}
		let lvds = new ListView.DataSource({rowHasChanged: (r1,r2) => r1 !== r2});
		this.state = {
			ds: lvds,
			count: this.props.rows.length,
			dataSource: lvds.cloneWithRows(this.props.rows)
		};
	}

	componentWillReceiveProps(props){
		//	Check if new was added
		//console.log("this.state.count: ", this.state.count, "... props.rows.count: ", props.rows.length);
		if(this.state.count < props.rows.length){
			//	new
			console.log("NEW!");
			this.refs.listView.scrollToEnd({animated:true});
		}
		if(this.props.rows !== props.rows){
			//console.log("NEW ROWS!: ", props);
			this.setState({
				dataSource: this.state.ds.cloneWithRows(props.rows),
				count: props.rows.length
			});
		}
	}

	row(data, rowID){
		return(
			<View style={styles.row}>
				<DefaultText text={data.firstName} />
				<DefaultText text={data.lastName} />

				<Button 
					title="edit"
					onPress={() => {this.props.actions.editItem(rowID)}}/>
			</View>
		);
	}

	render() {
		//console.log(this.state.dataSource);
		//const ds = new ListView.DataSource({ rowHasChanged: (r1, r2) => r1 !== r2 });
		return (
			<View 
				style={containerStyle.defaultContainer}>
				<ListView
					ref="listView"
					style={styles.listStyle}
					initialListSize={1}
					pageSize={3}
					dataSource={this.state.dataSource}
					renderRow={(data, sectionID, rowID) => this.row(data,rowID)}
					renderSeparator={(sectionID, rowID) => <View key={rowID} style={styles.separator}/> }
				/>
			</View>
		);
	}
}
// {console.log(data);return(<LongListRow data={data} rowID={rowID}/>)}
const styles = StyleSheet.create({
	listStyle: {
		overflow: "hidden"
	},
	separator: {
		flex: 1,
		height: StyleSheet.hairlineWidth,
		backgroundColor: "#222",
	},
})

export default connect((state) => ({
	rows: state.LongListReducer
}), (dispatch) => ({
	actions: bindActionCreators(LongListActions, dispatch)
})
)(LongList);

