/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { PureComponent } from 'react';
import {
	Text,
	View,
	Button,
	TouchableHighlight,
	StyleSheet,
	Animated
} from 'react-native';

import Swipeable from 'react-native-swipeable';

import Dimensions from 'Dimensions';

export default class ListRow extends PureComponent {
	
	componentWillMount(){
		this.rightValue = new Animated.Value(0);
	}

	animate(){
		var wWidth = Dimensions.get('window').width;
		Animated.timing(
			this.rightValue,
			{
				toValue: wWidth,
				duration: 300
			}
		).start(() => {
			this.rightValue.setValue(0);
			this.props.deleteItem(this.props.data._id);
		});
	}

	animates(){
		var wWidth = Dimensions.get('window').width;
		return Animated.timing(
			this.rightValue,
			{
				toValue: wWidth,
				duration: 300
			}
		);
	}

	render() {
		//console.log("ROW:", this.props);
		const {data, index, editItem, deleteItem} = this.props;

		const rightButtons = [
			<TouchableHighlight
				style={
					{
						justifyContent: 'center',
						backgroundColor: "#f95757",
						flex: 1,
						paddingLeft: 20,
						marginLeft: 8
					}
				}
				onPress={() => this.animate()}
			>
				<Text>Delete</Text>

			</TouchableHighlight>,
		];

		return (
			<Animated.View style={{right: this.rightValue, height: 75}}>

					<Text>
						{data.firstName}
					</Text>
					<Text>
						{data.lastName}
					</Text>
					<View
						style={{
							flex: 1,
							flexDirection: 'row',
							justifyContent: 'flex-end'
						}}
					>
						<Button
							title="Edit"
							onPress={() =>
								//start = performance.now();
								this.props.editItem(index)
							}
						/>
						<Button
							title="Delete"
							onPress={() => this.animate()}/>
					</View>

			</Animated.View>
		);
	}
}



