using System.Windows.Input;

namespace AssignmentMVVM.ViewModel.Commands
{
    public class GalleryCommand : ICommand
    {
        private readonly Action action;

        public event EventHandler CanExecuteChanged
        {
            add { CommandManager.RequerySuggested += value; }
            remove { CommandManager.RequerySuggested -= value; }
        }

        public GalleryCommand(Action action)
        {
            this.action = action;
        }

        public bool CanExecute(object parameter) => true;

        public void Execute(object parameter)
        {
            action();
        }
    }
}