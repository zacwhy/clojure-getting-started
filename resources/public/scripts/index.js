const e = React.createElement
const h = hyperscriptHelpers(React.createElement)

class EntryForm extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      transactionDate: props.transactionDate.toLocaleDateString('en-CA'),
      amount: '',
      fromAccount: '',
      toAccount: '',
      description: ''
    }

    this.handleChange = this.handleChange.bind(this)
    this.handleSubmit = this.handleSubmit.bind(this)
  }

  handleChange({target: {id, value}}) {
    this.setState({[id]: value})
  }

  handleSubmit(event) {
    event.preventDefault()
    const entry = {...this.state, amount: parseInt(this.state.amount)}
    this.props.onSubmit(entry)
  }

  render() {
    return h.form({onSubmit: this.handleSubmit},
      h.input({
        id: 'amount',
        min: 1,
        onChange: this.handleChange,
        placeholder: 'amount',
        required: true,
        step: 1,
        type: 'number',
        value: this.state.amount
      }),
      h.input({
        id: 'fromAccount',
        onChange: this.handleChange,
        placeholder: 'from',
        required: true,
        type: 'text',
        value: this.state.fromAccount
      }),
      h.input({
        id: 'toAccount',
        onChange: this.handleChange,
        placeholder: 'to',
        required: true,
        type: 'text',
        value: this.state.toAccount
      }),
      h.input({
        id: 'description',
        onChange: this.handleChange,
        placeholder: 'description',
        required: true,
        type: 'text',
        value: this.state.description
      }),
      h.input({
        id: 'transactionDate',
        onChange: this.handleChange,
        required: true,
        type: 'date',
        value: this.state.transactionDate
      }),
      h.input({type: 'submit', value: 'Submit'})
    )
  }
}

class EntryListItem extends React.Component {
  render() {
    const {
      amount,
      description,
      from_account: fromAccount,
      to_account: toAccount,
      transaction_date: transactionDate
    } = this.props.entry
    return h.div({},
      h.span({}, transactionDate),
      h.span({}, amount),
      h.span({}, fromAccount),
      h.span({}, toAccount),
      h.span({}, description)
    )
  }
}

class EntryList extends React.Component {
  render() {
    const entryListItems = this.props.entries.map(entry => e(EntryListItem, {entry}))
    return h.div({}, ...entryListItems)
  }
}

class App extends React.Component {
  constructor(props) {
    super(props)
    this.state = {entries: []}

    this.handleSubmit = this.handleSubmit.bind(this)
  }

  async componentDidMount() {
    const entries = await fetch('/entries').then(res => res.json())
    this.setState({entries})
  }

  async handleSubmit(entry) {
    const toSave = {
      transaction_date: entry.transactionDate,
      amount: entry.amount,
      from_account: entry.fromAccount,
      to_account: entry.toAccount,
      description: entry.description
    }
    console.log(toSave)
    const saved = await fetch('/entries', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(toSave)
    }).then(res => res.json())
    console.log(saved)
  }

  render() {
    const {entries} = this.state
    return h.div({},
      e(EntryForm, {
        onSubmit: this.handleSubmit,
        transactionDate: new Date()
      }),
      e(EntryList, {entries}))
  }
}

ReactDOM.render(e(App), document.getElementById('root'))

function hyperscriptHelpers(createElement) {
  const types = ['a', 'div', 'form', 'hr', 'input', 'nav', 'pre', 'section', 'span']
  return types.reduce((acc, type) => ({
    ...acc,
    [type]: (...args) => createElement(type, ...args)
  }), {})
}
