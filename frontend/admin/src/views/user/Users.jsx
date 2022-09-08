import { cilPen, cilTrash } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import { notification, Popconfirm, Space, Switch, Table, Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import userApi from '../../api/userApi';

const dataSource = [
    {
        id: '1',
        name: 'Mike',
        age: 32,
        address: '10 Downing Street',
        roles: [
            {
                id: 2,
                name: "USER"
            }
        ]
    },
    {
        id: '2',
        name: 'John',
        age: 42,
        address: '10 Downing Street',
        roles: [
            {
                id: 1,
                name: "ADMIN"
            }
        ]
    },
];

const Users = () => {
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
        },
        {
            title: 'Fullname',
            dataIndex: 'fullname',
            key: 'fullname',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (value) => <Switch checked={value} />
        },
        {
            title: 'Roles',
            dataIndex: 'roles',
            key: 'roles',
            render: (data) => data.map(item => (
                <Tag key={item.id}>
                    {item.name}
                </Tag>
            ))
        },
        {
            title: '',
            dataIndex: 'id',
            key: 'id',
            render: (id, res) => (
                <Space size={'large'}>
                    <Link to={"/user/update?id=" + id}>
                        <CIcon icon={cilPen} style={{ fontSize: 20 }} />
                    </Link>
                    {
                        res.roles.filter(role => role.name === 'ADMIN').length == 0 && (
                            <div style={{ cursor: 'pointer' }}>
                                <Popconfirm placement="top" title={`Delete user with id: ${id}`} onConfirm={() => onDelete(id)} okText="Yes" cancelText="No">
                                    <CIcon icon={cilTrash} style={{ fontSize: 20 }} />
                                </Popconfirm>
                            </div>
                        )
                    }

                </Space>
            )
        }
    ];


    const [users, setUsers] = useState([])
    const [isLoading, setIsLoading] = useState(false);
    const [response, setResponse] = useState({})

    useEffect(() => {
        load(1)
    }, [])

    const changePage = page => {
        load(page.current)
    }

    const onDelete = id => {
        userApi.delete(id).then(res => {
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            setUsers(prev => prev.filter(user => user.id !== id))

        }).catch(err => {
        })
    }

    const load = page => {
        console.log(page)
        setIsLoading(true)
        userApi.getAllByPage({ page }).then(res => {
            setUsers(res.content)
            setIsLoading(false)
            setResponse(res)
            console.log(res)
        })
    }

    return (
        <div>
            <Table rowKey={'id'} dataSource={users} columns={columns} loading={isLoading}
                pagination={{ total: response.totalElements, showSizeChanger: false }}
                onChange={changePage} />;
        </div>
    )
}

export default Users