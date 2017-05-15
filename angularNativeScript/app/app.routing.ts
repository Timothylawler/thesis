import HardwarePage from './pages/hardware/hardware.component';
import LongListPage from './pages/longList/longList.component';
import ContactsPage from './pages/contacts/contacts.component';
import CameraPhotoPage 	from './pages/cameraPhoto/cameraPhoto.component';
import CameraRecordPage from './pages/cameraRecord/cameraRecord.component';
 
export const Routes = [
	{path: "", component: HardwarePage},
	{path: "list", component: LongListPage},
	{path: "contacts", component: ContactsPage},
	{path: "cameraPhoto", component: CameraPhotoPage},
	{path: "cameraRecord", component: CameraRecordPage},
];

export const NavigatableComponents = [
	HardwarePage,
	LongListPage,
	ContactsPage,
	CameraPhotoPage,
	CameraRecordPage,
]