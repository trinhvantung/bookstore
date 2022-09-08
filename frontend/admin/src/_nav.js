import {
  cilBank,
  cilBook, cilChartPie, cilFolder, cilFont, cilImage, cilSpeedometer, cilStar,
  cilUser
} from '@coreui/icons'
import CIcon from '@coreui/icons-react'
import { CNavGroup, CNavItem, CNavTitle } from '@coreui/react'
import React from 'react'

const _nav = [
  {
    component: CNavItem,
    name: 'Dashboard',
    to: '/dashboard',
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />
  },
  {
    component: CNavTitle,
    name: 'Manager',
  },
  // {
  //   component: CNavGroup,
  //   name: 'User',
  //   icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  //   items: [
  //     {
  //       component: CNavItem,
  //       name: 'Create',
  //       to: '/user/create',

  //     },
  //     {
  //       component: CNavItem,
  //       name: 'List',
  //       to: '/user',

  //     }
  //   ],
  // },
  ,
  {
    component: CNavItem,
    name: 'User',
    to: '/user',
    icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  },
  {
    component: CNavGroup,
    name: 'Category',
    icon: <CIcon icon={cilFolder} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: 'Create',
        to: '/category/create',

      },
      {
        component: CNavItem,
        name: 'List',
        to: '/category',

      }
    ],
  },
  {
    component: CNavGroup,
    name: 'Author',
    icon: <CIcon icon={cilFont} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: 'Create',
        to: '/author/create',

      },
      {
        component: CNavItem,
        name: 'List',
        to: '/author',

      }
    ],
  },
  {
    component: CNavGroup,
    name: 'Publisher',
    icon: <CIcon icon={cilBank} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: 'Create',
        to: '/publisher/create',

      },
      {
        component: CNavItem,
        name: 'List',
        to: '/publisher',

      }
    ],
  },
  {
    component: CNavGroup,
    name: 'Product',
    icon: <CIcon icon={cilImage} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: 'Create',
        to: '/product/create',

      },
      {
        component: CNavItem,
        name: 'List',
        to: '/product',

      }
    ],
  },
  {
    component: CNavItem,
    name: 'Order',
    icon: <CIcon icon={cilBook} customClassName="nav-icon" />,
    to: '/order'
  },
  {
    component: CNavItem,
    name: 'Charts',
    to: '/charts',
    icon: <CIcon icon={cilChartPie} customClassName="nav-icon" />,
  }, {
    component: CNavItem,
    name: 'Icon Free',
    to: '/icons/coreui-icons',
    badge: {
      color: 'success',
      text: 'NEW',
    },
    icon: <CIcon icon={cilStar} customClassName="nav-icon" />
  },
  {
    component: CNavTitle,
    name: 'Extras',
  },
  {
    component: CNavGroup,
    name: 'Pages',
    icon: <CIcon icon={cilStar} customClassName="nav-icon" />,
    items: [
      {
        component: CNavItem,
        name: 'Login',
        to: '/login',
      },
      {
        component: CNavItem,
        name: 'Register',
        to: '/register',
      },
      {
        component: CNavItem,
        name: 'Error 404',
        to: '/404',
      },
      {
        component: CNavItem,
        name: 'Error 500',
        to: '/500',
      },
    ],
  }
]

export default _nav
