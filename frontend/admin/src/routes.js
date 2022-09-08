import React from 'react'
import PublisherForm from './views/publisher/PublisherForm'
import Publishers from './views/publisher/Publishers'

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))
const Charts = React.lazy(() => import('./views/charts/Charts'))
const CoreUIIcons = React.lazy(() => import('./views/icons/coreui-icons/CoreUIIcons'))

const UserForm = React.lazy(() => import('./views/user/UserForm'))
const Users = React.lazy(() => import('./views/user/Users'))

const CategoryForm = React.lazy(() => import('./views/category/CategoryForm'))
const Categories = React.lazy(() => import('./views/category/Categories'))

const Products = React.lazy(() => import('./views/product/Products'))
const ProductForm = React.lazy(() => import('./views/product/ProductForm'))

const Authors = React.lazy(() => import('./views/author/Authors'))
const AuthorForm = React.lazy(() => import('./views/author/AuthorForm'))

const Orders = React.lazy(() => import('./views/order/Orders'))
const OrderDetail = React.lazy(() => import('./views/order/OrderDetail'))

const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/dashboard', name: 'Dashboard', element: Dashboard },
  { path: '/charts', name: 'Charts', element: Charts },
  { path: '/icons', exact: true, name: 'Icons', element: CoreUIIcons },
  { path: '/icons/coreui-icons', name: 'CoreUI Icons', element: CoreUIIcons },


  { path: '/user', name: 'Users', element: Users },
  // { path: '/user/create', name: 'Create', element: UserForm },
  { path: '/user/update', name: 'Update', element: UserForm },


  { path: '/category', name: 'Categories', element: Categories },
  { path: '/category/create', name: 'Create', element: CategoryForm },
  { path: '/category/update', name: 'Update', element: CategoryForm },


  { path: '/product', name: 'Products', element: Products },
  { path: '/product/create', name: 'Create', element: ProductForm },
  { path: '/product/update', name: 'Update', element: ProductForm },


  { path: '/author', name: 'Authors', element: Authors },
  { path: '/author/create', name: 'Create', element: AuthorForm },
  { path: '/author/update', name: 'Update', element: AuthorForm },
  
  { path: '/publisher', name: 'Publishers', element: Publishers },
  { path: '/publisher/create', name: 'Create', element: PublisherForm },
  { path: '/publisher/update', name: 'Update', element: PublisherForm },


  { path: '/order', name: 'Orders', element: Orders },
  { path: '/order/update', name: 'OrderDetail', element: OrderDetail },
]

export default routes
