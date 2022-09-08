import { cilPen, cilTrash } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import { notification, Popconfirm, Space, Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import orderApi from '../../api/orderApi';



const Orders = () => {

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Fullname',
            dataIndex: 'fullName',
            key: 'fullName',
        },
        {
            title: 'Address',
            dataIndex: 'address',
            key: 'address',
        },
        {
            title: 'Phone number',
            dataIndex: 'phoneNumber',
            key: 'phoneNumber'
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status'
        },
        {
            title: '',
            dataIndex: 'id',
            key: 'id',
            render: (id) => (
                <Space size={'large'}>
                    <Link to={"/order/update?id=" + id}>
                        <CIcon icon={cilPen} style={{ fontSize: 20 }} />
                    </Link>
                    <div style={{ cursor: 'pointer' }}>
                        <Popconfirm placement="top" title={`Delete order with id: ${id}`} onConfirm={() => onDelete(id)} okText="Yes" cancelText="No">
                            <CIcon icon={cilTrash} style={{ fontSize: 20 }} />
                        </Popconfirm>
                    </div>
                </Space>
            )
        }
    ];

    const [orders, setOrders] = useState([])
    const [isLoading, setIsLoading] = useState(false);
    const [response, setResponse] = useState({})

    useEffect(() => {
        load(1)
    }, [])

    const changePage = page => {
        load(page.current)
    }

    const onDelete = id => {
        orderApi.delete(id).then(res => {
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            setOrders(prev => prev.filter(order => order.id !== id))

        }).catch(err => {
        })
    }

    const load = page => {
        console.log(page)
        setIsLoading(true)
        orderApi.getAllByPage({ page }).then(res => {
            setOrders(res.content)
            setIsLoading(false)
            setResponse(res)
            console.log(res)
        })
    }
    return (
        <div>
            <Table rowKey={'id'} dataSource={orders} columns={columns} loading={isLoading}
                pagination={{ total: response.totalElements, showSizeChanger: false }}
                onChange={changePage} />;
        </div>
    )
}

export default Orders